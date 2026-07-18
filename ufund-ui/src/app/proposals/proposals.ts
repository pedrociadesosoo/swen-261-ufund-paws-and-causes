import { Component, OnInit } from '@angular/core';
import { ProposalService } from '../proposal';
import { Proposal } from '../proposal.model';
import { AccountService } from '../account';
import { NeedService } from '../need';
import { Need } from '../need.model';

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
    this.selectedProposal = { ...proposal };
  }

  /**
   * Saves the edited proposal values as a new need and removes the proposal.
   */
  saveEditedProposal(): void {
    if (!this.selectedProposal) return;

    const approvedNeed: Need = {
      id: 0,
      name: this.selectedProposal.name,
      cost: this.selectedProposal.cost,
      quantity: this.selectedProposal.quantity,
      type: this.selectedProposal.type
    };

    this.needService.createNeed(approvedNeed).subscribe({
      next: () => {
        this.proposalService.deleteProposal(this.selectedProposal!.id).subscribe({
          next: () => {
            this.successMessage = `Need "${this.selectedProposal!.name}" approved and added to the cupboard`;
            this.errorMessage = '';
            this.selectedProposal = null;
            this.loadProposals();
          },
          error: () => {
            this.successMessage = '';
            this.errorMessage = 'Need was created, but removing the proposal failed';
          }
        });
      },
      error: () => {
        this.successMessage = '';
        this.errorMessage = 'Failed to approve proposal';
      }
    });
  }

    saveProposal(proposal: Proposal): void {

    const approvedNeed: Need = {
      id: 0,
      name: proposal.name,
      cost: proposal.cost,
      quantity: proposal.quantity,
      type: proposal.type
    };

    this.needService.createNeed(approvedNeed).subscribe({
      next: () => {
        this.proposalService.deleteProposal(proposal.id).subscribe({
          next: () => {
            this.successMessage = `Need "${proposal.name}" approved and added to the cupboard`;
            this.errorMessage = '';
            this.loadProposals();
          }
        });
      }
    });
  }
        

  /**
   * Cancels inline editing.
   */
  cancelEdit(): void {
    this.selectedProposal = null;
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




    NeedsValues = [
      "Name",
      "Cost",
      "Quantity",
      "Type",
      "Organization",
      "Submitted By",
      "Date",
      "Actions"
    ];

    proposalValues = [
      "name",
      "cost",
      "quantity",
      "type",
      "organization",
      "username",
      "creationDate"
    ];

}

