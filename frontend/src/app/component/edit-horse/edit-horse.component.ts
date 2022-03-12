import { Component, Input, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Location } from "@angular/common";

import { Horse } from "src/app/dto/horse";
import { HorseService } from "src/app/service/horse.service";
import { sexes as HORSESEXES } from "src/app/dto/horseSex";

@Component({
  selector: "app-edit-horse",
  templateUrl: "./edit-horse.component.html",
  styleUrls: ["./edit-horse.component.scss"],
})
export class EditHorseComponent implements OnInit {
  @Input() horse?: Horse;
  SEXES = HORSESEXES;

  constructor(
    private route: ActivatedRoute,
    private horseService: HorseService,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.getHorse();
  }
  getHorse(): void {
    const id = Number(this.route.snapshot.paramMap.get("id"));
    this.horseService.getHorse(id).subscribe((horse) => (this.horse = horse));
    console.log("got horse");
  }

  // goBack(): void {
  //   this.location.back();
  // }
  // save(): void {
  //   if (this.horse) {
  //     this.horseService.updateHorse(this.horse).subscribe(() => this.goBack());
  //   }
}
