import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Proposal } from './proposal.model';

/**
 * Handles HTTP communication with the proposals backend endpoints.
 */
@Injectable({
  providedIn: 'root'
})
export class ProposalService {
  private apiUrl = 'http://localhost:8080/proposals';

  constructor(private http: HttpClient) {}

  /**
   * Gets all proposals, or searches by partial name if one is given
   */
  getProposals(name?: string): Observable<Proposal[]> {
    if (name) {
      return this.http.get<Proposal[]>(`${this.apiUrl}?name=${name}`);
    }
    return this.http.get<Proposal[]>(this.apiUrl);
  }

  /**
   * Gets a single proposal by id
   */
  getProposalById(id: number): Observable<Proposal> {
    return this.http.get<Proposal>(`${this.apiUrl}/${id}`);
  }

  /**
   * Casts a vote on a proposal. Voting the same way again removes the vote,
   * voting the opposite way flips it. Returns the updated proposal.
   */
  vote(id: number, username: string, vote: number): Observable<Proposal> {
    return this.http.post<Proposal>(`${this.apiUrl}/${id}/vote`, { username, vote });
  }
}
