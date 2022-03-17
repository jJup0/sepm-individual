import { Component, Input, OnInit } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Horse } from "src/app/dto/horse";
import { HorseFormType } from "src/app/dto/horseFormTypeEnum";
import { HorseService } from "src/app/service/horse.service";

@Component({
  selector: "app-detail-horse",
  templateUrl: "./detail-horse.component.html",
  styleUrls: ["./detail-horse.component.scss"],
})
export class DetailHorseComponent implements OnInit {
  FORMTYPE: HorseFormType = "detail";
  horse?: Horse;
  deleted = false;

  constructor(
    private service: HorseService,
    private route: ActivatedRoute,
    private horseService: HorseService
  ) {}

  ngOnInit(): void {
    this.getHorse();
  }

  getHorse(): void {
    this.route.params.subscribe({
      next: (params) => {
        this.horseService.getHorse(params.id).subscribe({
          next: (horse) => {
            this.horse = horse;
            this.horse.birthdate = new Date(this.horse.birthdate);
          },
        });
      },
    });
  }

  deleteHorse(id: number) {
    this.service.deleteHorse(id).subscribe();
    this.deleted = true;
  }
}
