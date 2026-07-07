import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AccountService } from '../account';
@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {
	username: string = '';
	password: string = '';
	statusMsg: string = '';
	constructor(private accountService: AccountService, private router: Router){}
	
	login(): void {
		this.accountService.login(this.username, this.password).subscribe({
			next:(valid) => {

			this.accountService.setCurrentAccount(valid);
			this.statusMsg = `Successfully logged in as ${valid.username}`;
			},
			error:(err) => {
			if (err.status === 404){
				this.statusMsg = "Error: User does not exist";
			}
			else if(err.status === 401){
				this.statusMsg = "Error: invalid credentials";
			}
			else{
				this.statusMsg = "Unknown Error Occurred";
			}
			}
		
		});
	}
	createAccount(): void {
		this.accountService.createUser(this.username, this.password).subscribe({
			next:(valid) => {
			this.statusMsg = `User ${valid.username} successfully created`;
			},
			error:(err) => {
			if(err.status === 400){
			this.statusMsg = "Make sure your username matches the username requirements";
			}
			else if(err.status === 409){
			this.statusMsg = "Error: This username is already in use";
			}
			else{
			this.statusMsg = "Unknown Error Occurred";
			}
			}

		
		
		});
}
}
