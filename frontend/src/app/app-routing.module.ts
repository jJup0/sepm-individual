import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AddHorseComponent } from "./component/add-horse/add-horse.component";
import { EditHorseComponent } from "./component/edit-horse/edit-horse.component";
import { HorseComponent } from "./component/horse/horse.component";

const routes: Routes = [
  { path: "", redirectTo: "horses", pathMatch: "full" },
  { path: "horses", component: HorseComponent },
  { path: "add", component: AddHorseComponent },
  { path: "edit/:id", component: EditHorseComponent },
  { path: "**", redirectTo: "horses" },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
