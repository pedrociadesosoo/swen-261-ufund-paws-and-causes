import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedTypeDropdown } from './need-type-dropdown';

describe('NeedTypeDropdown', () => {
  let component: NeedTypeDropdown;
  let fixture: ComponentFixture<NeedTypeDropdown>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NeedTypeDropdown]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NeedTypeDropdown);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
