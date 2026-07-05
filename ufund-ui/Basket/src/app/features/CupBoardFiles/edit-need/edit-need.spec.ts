import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditNeed } from './edit-need';

describe('EditNeed', () => {
  let component: EditNeed;
  let fixture: ComponentFixture<EditNeed>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditNeed],
    }).compileComponents();

    fixture = TestBed.createComponent(EditNeed);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
