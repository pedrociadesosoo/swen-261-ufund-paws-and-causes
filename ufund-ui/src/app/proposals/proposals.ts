import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ProposalService } from '../proposal';
import { Proposal } from '../proposal.model';
import { AccountService } from '../account';
import { NeedService } from '../need';
import { Need } from '../need.model';
import { NeedType } from '../need-type';
import { HostListener } from '@angular/core';

/**
 * Lists all pending proposals so any logged-in user can see what's been submitted for review.
 */
@Component({
  selector: 'app-proposals',
  standalone: false,
  templateUrl: './proposals.html',
  styleUrl: './proposals.css',
})
export class Proposals implements OnInit {
  proposals: Proposal[] = [];
  errorMessage: string = '';
  successMessage: string = '';
  selectedProposal: any;
  originalProposal: any;
  NeedType = NeedType;

  /** The proposals shown in the table, after filtering and sorting */
  filteredProposals: Proposal[] = [];

  /** Search box: partial, case-insensitive match on the proposal name */
  searchTerm: string = '';
  /** Type dropdown filter; '' means "any type" */
  filterType: string = '';
  /** Organization dropdown filter; '' means "any organization" */
  filterOrg: string = '';
  /** The distinct organizations found in the proposals, for the Org dropdown */
  orgOptions: string[] = [];

  /** Column currently sorted by; '' means unsorted (original order) */
  sortField: string = '';
  /** Sort direction for the active column */
  sortDirection: 'asc' | 'desc' = 'asc';
  /** Only these columns can be clicked to sort */
  private readonly sortableFields = ['name', 'cost', 'quantity', 'type', 'organization', 'creationDate'];

  constructor(
    private proposalService: ProposalService,
    private accountService: AccountService,
    private needService: NeedService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadProposals();
  }

  /**
   * Loads all proposals from the API.
   */
  loadProposals(): void {
    this.proposalService.getProposals().subscribe({
      next: (proposals) => {
        this.proposals = proposals;
        // Distinct, non-empty org names for the Organization filter dropdown
        this.orgOptions = [...new Set(
          proposals.map(p => p.organization).filter(org => !!org)
        )];
        this.applyFilters();
      },
      error: () => this.errorMessage = 'Failed to load proposals'
    });
  }

  /**
   * Applies the search box, the Type/Org dropdowns, and the current sort to
   * produce the list shown in the table. Called whenever any of those change.
   */
  applyFilters(): void {
    const term = this.searchTerm.trim().toLowerCase();

    let result = this.proposals.filter(p => {
      const matchesName = !term || p.name.toLowerCase().includes(term);
      const matchesType = !this.filterType || p.type === this.filterType;
      const matchesOrg = !this.filterOrg || p.organization === this.filterOrg;
      return matchesName && matchesType && matchesOrg;
    });

    if (this.sortField) {
      const direction = this.sortDirection === 'asc' ? 1 : -1;
      result = result.sort((a, b) => this.compare(a, b, this.sortField) * direction);
    }

    this.filteredProposals = result;
  }

  /**
   * Handles a click on a sortable column header. Clicking the active column
   * toggles asc/desc; clicking a new column sorts it ascending.
   */
  setSort(field: string): void {
    if (!this.isSortable(field)) return;

    if (this.sortField === field) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortField = field;
      this.sortDirection = 'asc';
    }
    this.applyFilters();
  }

  /** True for the six columns the user is allowed to sort by. */
  isSortable(field: string): boolean {
    return this.sortableFields.includes(field);
  }

  /** Resets the search box and both dropdowns, then refreshes the list. */
  clearFilters(): void {
    this.searchTerm = '';
    this.filterType = '';
    this.filterOrg = '';
    this.applyFilters();
  }

  /**
   * Orders two proposals by one field for an ascending sort: numbers by value,
   * dates by time, everything else alphabetically. The caller flips the sign
   * for a descending sort.
   */
  private compare(a: Proposal, b: Proposal, field: string): number {
    if (field === 'cost' || field === 'quantity') {
      return a[field] - b[field];
    }
    if (field === 'creationDate') {
      return new Date(a[field]).getTime() - new Date(b[field]).getTime();
    }
    return String(a[field] ?? '').localeCompare(String(b[field] ?? ''));
  }



  /**
   * Manager-only: deletes a proposal after confirmation.
   */
  deleteProposal(proposal: Proposal): void {
    if (!confirm(`Delete "${proposal.name}" from the proposals list?`)) return;
    this.proposalService.deleteProposal(proposal.id).subscribe({
      next: () => {
        this.successMessage = `${proposal.name} deleted`;
        this.errorMessage = '';
        this.loadProposals();
      },
      error: (err: any) => {
        this.successMessage = '';
        this.errorMessage = err.error || 'Failed to delete proposal';
      }
    });
  }


  /**
   * Navigates to the proposal's detail view, unless it's currently being
   * edited inline (in which case clicking the row shouldn't navigate away).
   */
  onRowClick(proposal: Proposal): void {
    if (this.selectedProposal?.id === proposal.id) return;
    this.router.navigate(['/proposals', proposal.id]);
  }

  /**
   * Opens the inline editor for a proposal.
   */
  editProposal(proposal: Proposal): void {
    this.originalProposal = { ...proposal };
    this.selectedProposal = { ...proposal };
  }

  /**
   * Saves the edited proposal values as a new need and removes the proposal.
   */
  saveEditedProposal(proposal: Proposal): void {
    if (!confirm(`Save and attempt approval for "${proposal.name}"?`)) return;

    this.proposalService.updateProposal(proposal).subscribe({
      next: (updatedProposal) => {
        this.proposalService.approveProposal(updatedProposal.id).subscribe({
          next: () => {
            this.successMessage = `Changes to "${proposal.name}" saved and approved successfully!`;
            this.errorMessage = '';
            this.loadProposals();
            this.cancelEdit();
          },
          error: (err: any) => {
            this.successMessage = ``;
            this.errorMessage = err.error || `Changes to ${proposal.name} were saved, but approval failed.`;
            this.loadProposals();
            this.cancelEdit();
          }
        });
      },
      error: (err: any) => {
        this.errorMessage = err.error || 'Failed to save changes';
        this.successMessage = '';
        this.loadProposals();
      }
    });
  }

  approveProposal(proposal: Proposal): void {

    if (!confirm(`approve "${proposal.name}" from the proposals list?`)) return;

    this.proposalService.approveProposal(proposal.id).subscribe({
          next: () => {
            this.successMessage = `Need "${proposal.name}" approved and added to the cupboard`;
            this.errorMessage = '';
            this.loadProposals();
          },
        error: (err: any) => {
          this.successMessage = '';
          this.errorMessage = err.error || "failed to approve proposal";
        }
    });
  }

  rejectProposal(proposal: Proposal): void {
    if (!confirm(`Reject "${proposal.name}"?`)) return;

    this.proposalService.rejectProposal(proposal.id).subscribe({
      next: () => {
        this.successMessage = `"${proposal.name}" rejected`;
        this.errorMessage = '';
        this.loadProposals();
      },
      error: (err: any) => {
        this.errorMessage = err.error || 'Failed to reject proposal';
        this.successMessage = '';
      }
    });
  }

        

  /**
   * Cancels inline editing.
   */
  cancelEdit(): void {
    this.selectedProposal = null;
    this.originalProposal = null;
  }


  /**
   * Returns true if the current user is a manager
   */
  isManager(): boolean {
    return this.accountService.isManager();
  }

  /**
   * Returns true if the current user is a helper
   */
  isHelper(): boolean {
    return this.accountService.isHelper();
  }

  /**
   * Returns the net vote total (+1 per upvote, -1 per downvote) for a proposal
   */
  netVotes(proposal: Proposal): number {
    return Object.values(proposal.votes || {}).reduce((sum, v) => sum + v, 0);
  }

  /**
   * Returns the current user's vote on a proposal: 1, -1, or undefined if
   * they haven't voted
   */
  myVote(proposal: Proposal): number | undefined {
    const username = this.accountService.getCurrentAccount()?.username;
    if (!username) return undefined;
    return proposal.votes ? proposal.votes[username] : undefined;
  }

  /**
   * Casts an upvote. Voting up again removes the vote; voting down then up flips it.
   */
  upvote(proposal: Proposal): void {
    this.castVote(proposal, 1);
  }

  /**
   * Casts a downvote. Voting down again removes the vote; voting up then down flips it.
   */
  downvote(proposal: Proposal): void {
    this.castVote(proposal, -1);
  }

  private castVote(proposal: Proposal, value: number): void {
    const username = this.accountService.getCurrentAccount()?.username;
    if (!username) return;

    this.proposalService.vote(proposal.id, username, value).subscribe({
      next: (updated) => {
        const index = this.proposals.findIndex(p => p.id === updated.id);
        if (index !== -1) this.proposals[index] = updated;
        this.applyFilters();
      },
      error: () => this.errorMessage = 'Failed to cast vote'
    });
  }



  get hasUnsavedChanges(): boolean {
      const current = this.selectedProposal;
      const original = this.originalProposal;

      if (!current || !original) return false;

      return (['name', 'cost', 'quantity', 'type'] as const).some(
        key => current[key] !== original[key]
      );
  }

  canDeactivate(): boolean {
    return !this.hasUnsavedChanges;
  }

  getVoteDisplay(votes: Map<string, number>): string {
    if (!votes || Object.keys(votes).length === 0) {
      return 'No votes yet';
    }

    return Object.entries(votes)
      .map(([key, value]) => `${key}: ${value}`)
      .join(', ');
  }

    @HostListener('window:beforeunload', ['$event'])
    handleBeforeUnload(event: BeforeUnloadEvent) {
      if (this.hasUnsavedChanges) {
        event.preventDefault();
      }
    }



    NeedsValues = [
      "Name",
      "Cost",
      "Quantity",
      "Type",
      "Organization",
      "Submitted By",
      "Date",
      "votes",
      "status"
    ];

    proposalValues = [
      "name",
      "cost",
      "quantity",
      "type",
      "organization",
      "username",
      "creationDate",
      "votes",
      "status"
    ];

}

