import { Component, OnInit } from "@angular/core";
import { Horse } from "../../dto/horse";
import { HorseService } from "src/app/service/horse.service";

@Component({
  selector: "app-horse",
  templateUrl: "./horse.component.html",
  styleUrls: ["./horse.component.scss"],
})
export class HorseComponent implements OnInit {
  search = false;
  horses: Horse[];
  error: string = null;

  constructor(private service: HorseService) {}

  ngOnInit(): void {
    this.reloadHorses();
  }

  reloadHorses() {
    this.service.getAllHorses().subscribe({
      next: (data) => {
        console.log("received horses", data);
        this.horses = data;
      },
      error: (error) => {
        console.error("Error fetching horses", error.message);
        this.showError("Could not fetch horses: " + error.message);
      },
    });
  }

  deleteHorse(id: number) {
    this.service.deleteHorse(id).subscribe();
    // TODO if no error, any nicer way?
    this.horses = this.horses.filter((item) => item.id !== id);
  }

  public vanishError(): void {
    this.error = null;
  }

  private showError(msg: string) {
    this.error = msg;
  }
}
