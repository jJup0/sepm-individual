import { Component, OnInit } from "@angular/core";
import { Owner } from "src/app/dto/owner";
import { OwnerService } from "src/app/service/owner.service";

@Component({
  selector: "app-owner",
  templateUrl: "./owner.component.html",
  styleUrls: ["./owner.component.scss"],
})
export class OwnerComponent implements OnInit {
  // search = false;
  owners: Owner[];
  // error: string = null;

  constructor(private service: OwnerService) {}

  ngOnInit(): void {
    this.reloadHorses();
  }

  reloadHorses() {
    this.service.getAllOwners().subscribe({
      next: (data) => {
        console.log("received horses", data);
        this.owners = data;
      },
      error: (error) => {
        console.error("Error fetching horses", error.message);
      },
    });
  }
}
