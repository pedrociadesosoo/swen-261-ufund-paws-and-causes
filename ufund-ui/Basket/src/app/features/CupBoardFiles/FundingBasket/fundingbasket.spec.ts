import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FundingbasketComponent } from './fundingbasket';

describe('FundingbasketComponent', () => {
  let component: FundingbasketComponent;
  let fixture: ComponentFixture<FundingbasketComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FundingbasketComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FundingbasketComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
