import { TestBed } from '@angular/core/testing';

import { ConfigService } from './config.service';

fdescribe('ConfigService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ConfigService = TestBed.get(ConfigService);
    expect(service).toBeTruthy();
  });

  it('should set value', () => {
    const config = {
      'name': 'agnitra'
    };
    const service: ConfigService = TestBed.get(ConfigService);
    service.setConfig(config);
    expect(service.configs).toEqual(config);
  });

  it('should get expected value', () => {
    const config = {
      'name': 'agnitra'
    };
    const service: ConfigService = TestBed.get(ConfigService);
    service.setConfig(config);
    const x = service.getConfigByKey('name');
    expect(x).toEqual(config.name);
  })
});
