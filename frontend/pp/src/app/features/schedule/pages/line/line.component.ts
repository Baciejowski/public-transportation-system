import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-line',
  templateUrl: './line.component.html',
  styleUrls: ['./line.component.scss']
})
export class LineComponent implements OnInit {

  lineId: string;

  constructor(private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.lineId =  this.route.snapshot.paramMap.get('lineId')!;
  }

}
