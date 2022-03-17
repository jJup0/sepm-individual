import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { AddHorseComponent } from "./component/add-horse/add-horse.component";
import { DetailHorseComponent } from "./component/detail-horse/detail-horse.component";
import { EditHorseComponent } from "./component/edit-horse/edit-horse.component";
import { HorseComponent } from "./component/horse/horse.component";
import { SearchHorseComponent } from "./component/search-horse/search-horse.component";
import { OwnerComponent } from "./component/owner/owner.component";
import { HorseFamilytreeComponent } from "./component/horse-familytree/horse-familytree.component";

const routes: Routes = [
  { path: "", redirectTo: "horses", pathMatch: "full" },
  { path: "horses", component: HorseComponent },
  { path: "search", component: SearchHorseComponent },
  { path: "add", component: AddHorseComponent },
  { path: "edit/:id", component: EditHorseComponent },
  { path: "detail/:id", component: DetailHorseComponent },
  { path: "familytree/:id", component: HorseFamilytreeComponent },
  { path: "owners", component: OwnerComponent },
  { path: "**", redirectTo: "horses" },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
