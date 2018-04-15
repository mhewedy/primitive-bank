import { TestBed, inject } from '@angular/core/testing';

import { MessageService } from './message.service';

describe('MessageService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MessageService]
    });
  });

  it('should be created', inject([MessageService], (service: MessageService) => {
    expect(service).toBeTruthy();
  }));

  it('calling setAsSuccess sets a message and success to true', inject([MessageService], (service: MessageService) => {
    service.setAsSuccess("some message")
    expect(service.message).toBe("some message")
    expect(service.success).toBeTruthy()
  }));

  it('calling setAsError sets a message and success to false', inject([MessageService], (service: MessageService) => {
    service.setAsError("some message")
    expect(service.message).toBe("some message")
    expect(service.success).toBeFalsy()
  }));

  it('calling clear clears the message', inject([MessageService], (service: MessageService) => {
    service.setAsSuccess("some message")
    service.clear()
    expect(service.message).toBeNull()
  }));
});
