import { Component, Input, OnInit } from "@angular/core";
import { Horse } from "src/app/dto/horse";
import { HorseService } from "src/app/service/horse.service";

@Component({
  selector: "app-horse-table",
  templateUrl: "./horse-table.component.html",
  styleUrls: ["./horse-table.component.scss"],
})
export class HorseTableComponent implements OnInit {
  @Input()
  horses: Horse[];

  constructor(private service: HorseService) {}

  ngOnInit(): void {}

  dateToString(bd) {
    if (bd === null) {
      return "";
    }
    if (typeof bd == "string") {
      return bd;
    }
    return bd.toISOString().slice(0, 10);
  }

  deleteHorse(id: number) {
    this.service.deleteHorse(id).subscribe();
    // TODO if no error, any nicer way?
    this.horses = this.horses.filter((item) => item.id !== id);
  }
}
