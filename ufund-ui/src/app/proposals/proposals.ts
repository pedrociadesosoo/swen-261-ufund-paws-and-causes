import { Component, OnInit } from '@angular/core';
import { ProposalService } from '../proposal';
import { Proposal } from '../proposal.model';
import { AccountService } from '../account';



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


  constructor(
    private proposalService: ProposalService,
    private accountService: AccountService
  ) {}

  ngOnInit(): void {
    this.loadProposals();
  }

  /**
   * Loads all proposals from the API
   */
  loadProposals(): void {
    this.proposalService.getProposals().subscribe({
      next: (proposals) => this.proposals = proposals,
      error: () => this.errorMessage = 'Failed to load proposals'
    });
  }



    /**
     * Manager-only: deletes a need after confirmation, then reloads the list
     */
    deleteProposal(proposal: Proposal): void {
      if (!confirm(`Delete "${proposal.name}" from the cupboard?`)) return;
      this.proposalService.deleteProposal(proposal.id).subscribe({
        next: () => {
          this.successMessage = `${proposal.name} deleted`;
          this.errorMessage = '';
        },
        error: () => {
          this.successMessage = '';
          this.errorMessage = 'Failed to delete proposal';
        }
      });
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
}
