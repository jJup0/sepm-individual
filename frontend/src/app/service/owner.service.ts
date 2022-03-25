import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { Owner } from "../dto/owner";
import { OwnerSearchDto } from "../dto/ownerSearchDto";

const baseUri = environment.backendUrl + "/owners";

@Injectable({
  providedIn: "root",
})
export class OwnerService {
  httpOptions = {
    headers: new HttpHeaders({ "Content-Type": "application/json" }),
  };

  constructor(private http: HttpClient) {}

  getAllOwners(): Observable<Owner[]> {
    return this.http.get<Owner[]>(baseUri);
  }

  search(searchParameters: OwnerSearchDto): Observable<Owner[]> {
    const searchParametersStandardized = JSON.parse(
      JSON.stringify(searchParameters)
    );
    const urlSearchParams = new URLSearchParams(
      searchParametersStandardized
    ).toString();

    return this.http.get<Owner[]>(baseUri + "/selection?" + urlSearchParams);
  }
}
