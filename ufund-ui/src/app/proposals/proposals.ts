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
