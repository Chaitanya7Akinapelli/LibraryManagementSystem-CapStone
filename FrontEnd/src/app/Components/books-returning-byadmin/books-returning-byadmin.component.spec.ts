import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BooksReturningByadminComponent } from './books-returning-byadmin.component';

describe('BooksReturningByadminComponent', () => {
  let component: BooksReturningByadminComponent;
  let fixture: ComponentFixture<BooksReturningByadminComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BooksReturningByadminComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BooksReturningByadminComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
