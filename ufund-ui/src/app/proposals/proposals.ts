import { Component, OnInit } from '@angular/core';
import { ProposalService } from '../proposal';
import { Proposal } from '../proposal.model';
import { AccountService } from '../account';

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
      },
      error: () => this.errorMessage = 'Failed to cast vote'
    });
  }
}
