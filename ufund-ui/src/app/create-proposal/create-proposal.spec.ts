import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CreateProposal } from './create-proposal';

describe('CreateProposal', () => {
  let component: CreateProposal;
  let fixture: ComponentFixture<CreateProposal>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CreateProposal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CreateProposal);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
