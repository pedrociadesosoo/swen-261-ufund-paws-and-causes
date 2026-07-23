import { Component, OnInit } from '@angular/core';
import { ProposalService } from '../proposal';
import { Proposal } from '../proposal.model';
import { AccountService } from '../account';
import { NeedService } from '../need';
import { Need } from '../need.model';
import { HostListener } from '@angular/core';

/**
 * Lists all pending proposals so any logged-in user can see what's been
 * submitted for review.
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

  constructor(
    private proposalService: ProposalService,
    private accountService: AccountService,
    private needService: NeedService
  ) {}

  ngOnInit(): void {
    this.loadProposals();
  }

  /**
   * Loads all proposals from the API.
   */
  loadProposals(): void {
    this.proposalService.getProposals().subscribe({
      next: (proposals) => this.proposals = proposals,
      error: () => this.errorMessage = 'Failed to load proposals'
    });
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
      error: () => {
        this.successMessage = '';
        this.errorMessage = 'Failed to delete proposal';
      }
    });
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
          error: () => {
            this.successMessage = ``;
            this.errorMessage = `Changes to ${proposal.name} were saved, but approval failed.`;
            this.loadProposals();
            this.cancelEdit();
          }
        });
      },
      error: () => {
        this.errorMessage = 'Failed to save changes';
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
        error: () => {
          this.successMessage = '';
          this.errorMessage = "failed to approve proposal";
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
      error: () => {
        this.errorMessage = 'Failed to reject proposal';
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
      "allVotes",
      "status",
      "Actions"
    ];

    proposalValues = [
      "name",
      "cost",
      "quantity",
      "type",
      "organization",
      "username",
      "creationDate",
      "allVotes",
      "status"
    ];

}

