import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgForm } from '@angular/forms';
import { ViewChild } from '@angular/core';
import { ProposalService } from '../proposal';
import { Proposal } from '../proposal.model';
import { AccountService } from '../account';
import { NeedType } from '../need-type';
import { OrganizationService } from '../organization.service';
import { Organization } from '../organization';


@Component({
  selector: 'app-create-proposal',
  standalone: false,
  templateUrl: './create-proposal.html',
  styleUrl: './create-proposal.css',
})
export class CreateProposal implements OnInit {
  NeedType = NeedType;

  organizations: Organization[] = [];

  proposal: Proposal = {
    id: 0,
    name: '',
    cost: 0,
    quantity: 0,
    type: NeedType.SELECT,
    username: '',
    organization: '',
    creationDate: '',
    lastEdited: '',
    votes: {},
    status: ''
  };

  errorMessage: string = '';
  successMessage: string = '';

  private submitted: boolean = false;

  @ViewChild('proposalForm') proposalForm?: NgForm;

  constructor(
    private proposalService: ProposalService,
    private router: Router,
    private accountService: AccountService,
    private organizationService: OrganizationService
  ) {}

  ngOnInit(): void {
    this.organizationService.getOrganizationArray().subscribe({
      next: (orgs) => this.organizations = orgs
    });
  }

  //validates proposal input fields
  validate(): boolean {
	//A proposal must have a name
  	if(this.proposal.name.trim() === ''){
  		this.errorMessage = "Error: A proposal must have a name";
		return false;
  	}
	//A proposal's name cannot be over 100 characters
	if(this.proposal.name.length > 100){
		this.errorMessage = "Please keep the proposal's  name under 100 characters in length";
		return false;
	}
	//A proposal's cost cannot be negative
	if(this.proposal.cost <0){
		this.errorMessage = "A proposal's cost must not be negative";
		return false;
	}

	if(this.proposal.quantity <= 0){
		this.errorMessage = "A proposal's quantity must be a positive integer";
		return false;
	}
    	if (this.proposal.type == NeedType.SELECT) {
      		this.errorMessage = 'Type is required.';
      		return false;
    	}
	return true;
  }

  onSubmit():void{
	 this.errorMessage = '';
	  if(!this.validate()) return;
	 this.proposal.username = this.accountService.getCurrentAccount()?.username ?? '';
	  this.proposalService.createProposal(this.proposal).subscribe({
		  next: (created) => {
			  this.successMessage = `Proposal "${created.name}" added successfully!`;
			  this.submitted = true;
			  this.router.navigate(['/cupboard']);
		  },
		  error: (err) => {
			  if (err.status === 409)
				  this.errorMessage = 'You already have a pending proposal, please wait until your current proposal has been processed before submitting another';
			  else
				  this.errorMessage = 'Failed to add proposal. Please try again.';
		  }
    });
  }

  goToCupboard(): void {
    this.router.navigate(['/cupboard']);
  }
  /**
   * Called by unsavedChangesGuard before leaving this route. Safe to leave
   * silently if nothing was changed, or if the change was already saved;
   * otherwise the guard will prompt the user to confirm discarding it.
   */
  canDeactivate():boolean {
          return this.submitted || !this.proposalForm?.dirty;
  }


}
