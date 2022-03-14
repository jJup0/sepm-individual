import { Component, Input, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Location } from "@angular/common";

import { Horse } from "src/app/dto/horse";
import { HorseService } from "src/app/service/horse.service";
import { sexes as HORSESEXES } from "src/app/dto/horseSexEnum";
import { HorseFormType } from "src/app/dto/horseFormTypeEnum";

@Component({
  selector: "app-edit-horse",
  templateUrl: "./edit-horse.component.html",
  styleUrls: ["./edit-horse.component.scss"],
})
export class EditHorseComponent implements OnInit {
  horse?: Horse;
  FORMTYPE: HorseFormType = "edit";

  constructor(
    private route: ActivatedRoute,
    private horseService: HorseService
  ) {}

  ngOnInit(): void {
    this.getHorse();
  }
  getHorse(): void {
    const id = Number(this.route.snapshot.paramMap.get("id"));
    this.horseService.getHorse(id).subscribe((horse) => {
      this.horse = horse;
      this.horse.birthdate = new Date(this.horse.birthdate);
    });
  }

  // TODO add back button

  save(horse: Horse): void {
    this.horse = horse;
    this.horseService.updateHorse(this.horse).subscribe();
    console.log(`saved horse: ${JSON.stringify(this.horse)}`);
  }
}
