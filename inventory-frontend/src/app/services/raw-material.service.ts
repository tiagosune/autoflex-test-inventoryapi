import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RawMaterial } from '../models/raw-material';

@Injectable({
  providedIn: 'root',
})
export class RawMaterialService {

  private api = 'http://localhost:8080/api/raw-materials';

  constructor(private http: HttpClient) { }

  getAll() {
    return this.http.get<RawMaterial[]>(this.api);
  }

}
