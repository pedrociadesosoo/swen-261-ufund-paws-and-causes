import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddNeed } from './add-need';

describe('AddNeed', () => {
  let component: AddNeed;
  let fixture: ComponentFixture<AddNeed>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddNeed]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddNeed);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
