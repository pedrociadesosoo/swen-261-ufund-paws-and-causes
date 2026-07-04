import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NeedDetail } from './need-detail';

describe('NeedDetail', () => {
  let component: NeedDetail;
  let fixture: ComponentFixture<NeedDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NeedDetail]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NeedDetail);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
