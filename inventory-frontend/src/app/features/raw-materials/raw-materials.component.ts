import { Component, OnInit } from '@angular/core';
import { RawMaterial } from '../../models/raw-material';
import { RawMaterialService } from '../../services/raw-material.service';
import { Observable } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-raw-materials',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './raw-materials.component.html',
  styleUrl: './raw-materials.component.css',
})
export class RawMaterialsComponent implements OnInit {

  rawMaterials$!: Observable<RawMaterial[]>;

  constructor(private rawMaterialService: RawMaterialService) {}

  ngOnInit(): void {
    this.rawMaterials$ = this.rawMaterialService.getAll();
  }

}
