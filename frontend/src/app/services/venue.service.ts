import { Injectable } from "@angular/core";
import { Globals } from "../global/globals";

@Injectable({
  providedIn: "root",
})
export class VenueService {
  private userBaseUri: string = this.globals.backendUri + "/venue";

  constructor(private globals: Globals) {}
}
