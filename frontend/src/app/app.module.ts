import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";
import { FormsModule } from "@angular/forms";
import { HttpClientModule } from "@angular/common/http";
import { NgbModule } from "@ng-bootstrap/ng-bootstrap";

import { AppComponent } from "./app.component";
import { AppRoutingModule } from "./app-routing.module";
import { HeaderComponent } from "./component/header/header.component";
import { HorseComponent } from "./component/horse/horse.component";
import { AddHorseComponent } from "./component/add-horse/add-horse.component";
import { EditHorseComponent } from "./component/edit-horse/edit-horse.component";
import { HorseFormComponent } from "./component/horse-form/horse-form.component";
import { DetailHorseComponent } from "./component/detail-horse/detail-horse.component";
import { SearchHorseComponent } from "./component/search-horse/search-horse.component";
import { HorseTableComponent } from "./component/horse-table/horse-table.component";
import { OwnerComponent } from './component/owner/owner.component';
import { HorseFamilytreeComponent } from './component/horse-familytree/horse-familytree.component';
import { HorseFamilytreeRowComponent } from './component/horse-familytree/horse-familytree-row/horse-familytree-row.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HorseComponent,
    AddHorseComponent,
    EditHorseComponent,
    HorseFormComponent,
    DetailHorseComponent,
    SearchHorseComponent,
    HorseTableComponent,
    OwnerComponent,
    HorseFamilytreeComponent,
    HorseFamilytreeRowComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NgbModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
