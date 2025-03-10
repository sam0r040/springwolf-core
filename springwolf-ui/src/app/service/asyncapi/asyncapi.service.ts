/* SPDX-License-Identifier: Apache-2.0 */
import { AsyncApi } from "../../models/asyncapi.model";
import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable, shareReplay, switchMap } from "rxjs";
import { filter, map, tap } from "rxjs/operators";
import { EndpointService } from "../endpoint.service";
import { AsyncApiMapperService } from "./asyncapi-mapper.service";
import { ServerAsyncApi } from "./models/asyncapi.model";
import { IUiService } from "../ui.service";
import { AsyncApiValidatorService } from "./validator/asyncapi-validator.service";

@Injectable()
export class AsyncApiService {
  private readonly docs: Observable<AsyncApi>;

  constructor(
    private http: HttpClient,
    private asyncApiMapperService: AsyncApiMapperService,
    private uiService: IUiService,
    private asyncApiValidatorService: AsyncApiValidatorService
  ) {
    this.docs = this.uiService.isGroup$.pipe(
      switchMap((group) => {
        const url =
          group == IUiService.DEFAULT_GROUP
            ? EndpointService.docs
            : EndpointService.getDocsForGroupEndpoint(group);
        return this.http.get<ServerAsyncApi>(url);
      }),
      tap((item) => this.asyncApiValidatorService.validate(item)),
      map((item) => this.asyncApiMapperService.toAsyncApi(item)),
      filter((item): item is AsyncApi => item !== undefined),
      shareReplay()
    );
  }

  public getAsyncApi(): Observable<AsyncApi> {
    return this.docs;
  }
}
