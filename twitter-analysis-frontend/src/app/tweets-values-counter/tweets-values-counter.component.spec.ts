import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TweetsValuesCounterComponent } from './tweets-values-counter.component';

describe('TweetsValuesCounterComponent', () => {
  let component: TweetsValuesCounterComponent;
  let fixture: ComponentFixture<TweetsValuesCounterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TweetsValuesCounterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TweetsValuesCounterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
