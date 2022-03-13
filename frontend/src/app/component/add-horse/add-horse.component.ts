import { Component, OnInit } from "@angular/core";

import { Horse } from "../../dto/horse";
import { HorseService } from "src/app/service/horse.service";
import { HorseFormType } from "src/app/dto/horseFormTypeEnum";

@Component({
  selector: "app-add-horse",
  templateUrl: "./add-horse.component.html",
  styleUrls: ["./add-horse.component.scss"],
})
export class AddHorseComponent implements OnInit {
  FORMTYPE: HorseFormType = "add";
  horse?: Horse;

  constructor(private service: HorseService) {}

  ngOnInit(): void {
    this.resetHorse();
  }

  resetHorse() {
    this.horse = {
      name: "Horse",
      description: "",
      birthdate: new Date(),
      sex: "female",
      owner: null,
      motherId: null,
      fatherId: null,
    };
  }

  submit(horse: Horse): void {
    this.horse = horse;
    this.postHorse();
    this.resetHorse();
  }

  postHorse(): void {
    this.service.addHorse(this.horse).subscribe({
      next: (data) => {
        console.log("added horse", data);
      },
      error: (error) => {
        console.error("Error adding horse", error.message);
        // TODO: show user
      },
    });
  }
}
