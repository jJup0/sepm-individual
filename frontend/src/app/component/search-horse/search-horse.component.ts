import { Component, OnInit } from "@angular/core";
import { Horse } from "src/app/dto/horse";
import { HorseFormType } from "src/app/dto/horseFormTypeEnum";
import { HorseSearchDto } from "src/app/dto/horseSearchDto";
import { HorseService } from "src/app/service/horse.service";

@Component({
  selector: "app-search-horse",
  templateUrl: "./search-horse.component.html",
  styleUrls: ["./search-horse.component.scss"],
})
export class SearchHorseComponent implements OnInit {
  FORMTYPE: HorseFormType = "search";
  searchFields: Horse = {
    name: null,
    description: null,
    birthdate: null,
    sex: null,
    owner: null,
  };
  matchedHorses: Horse[] = [];
  gotHorses = false;

  constructor(private service: HorseService) {}

  ngOnInit(): void {}

  search(horse: Horse): void {
    const horseSearchDto: HorseSearchDto = {
      name: horse.name,
      description: horse.description,
      bornAfter:
        horse.birthdate === null
          ? null
          : new Date(horse.birthdate).toISOString().slice(0, 10),
      sex: horse.sex,
      bornBefore: null,
      owner: horse.owner,
    };

    this.service.search(horseSearchDto).subscribe({
      next: (data) => {
        console.log("received horses", data);
        this.matchedHorses = data;
        this.gotHorses = true;
      },
      error: (error) => {
        console.error("Error fetching horses", error.message);
      },
    });
  }
}
