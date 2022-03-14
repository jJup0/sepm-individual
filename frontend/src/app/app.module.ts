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
import { DetailHorseComponent } from './component/detail-horse/detail-horse.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    HorseComponent,
    AddHorseComponent,
    EditHorseComponent,
    HorseFormComponent,
    DetailHorseComponent,
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
