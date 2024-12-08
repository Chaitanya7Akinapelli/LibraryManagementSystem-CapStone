import { TestBed } from '@angular/core/testing';

import { UserStatisticsServiceService } from './user-statistics-service.service';

describe('UserStatisticsServiceService', () => {
  let service: UserStatisticsServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserStatisticsServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
