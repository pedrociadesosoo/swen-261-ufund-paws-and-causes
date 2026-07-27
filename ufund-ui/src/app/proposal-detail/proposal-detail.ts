import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProposalService } from '../proposal';
import { Proposal } from '../proposal.model';

/**
 * Shows the full details of a single proposal.
 */
@Component({
  selector: 'app-proposal-detail',
  standalone: false,
  templateUrl: './proposal-detail.html',
  styleUrl: './proposal-detail.css',
})
export class ProposalDetail implements OnInit {
  proposal: Proposal | null = null;
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private proposalService: ProposalService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.proposalService.getProposalById(id).subscribe({
      next: (proposal) => this.proposal = proposal,
      error: () => this.errorMessage = 'Failed to load proposal details'
    });
  }

  /**
   * Returns the net vote total (+1 per upvote, -1 per downvote) for the proposal
   */
  netVotes(): number {
    if (!this.proposal || !this.proposal.votes) return 0;
    return Object.values(this.proposal.votes).reduce((sum, v) => sum + v, 0);
  }

  /**
   * Navigates back to the proposals list
   */
  goBack(): void {
    this.router.navigate(['/proposals']);
  }
}
