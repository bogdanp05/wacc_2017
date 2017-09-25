import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { FlexLayoutModule } from '@angular/flex-layout';
import { AppComponent } from './app.component';
import { TweetsListComponent } from './tweets-list/tweets-list.component';
import { FilterButtonsComponent } from './filter-buttons/filter-buttons.component';
import { TweetsValuesCounterComponent } from './tweets-values-counter/tweets-values-counter.component';

// Imports for loading & configuring the in-memory web api
import { InMemoryWebApiModule } from 'angular-in-memory-web-api';
import { InMemoryDataService }  from './in-memory-data.service';

@NgModule({
  declarations: [
    AppComponent,
    TweetsListComponent,
    FilterButtonsComponent,
    TweetsValuesCounterComponent
  ],
  imports: [
    BrowserModule,
    NgbModule.forRoot(),
    FlexLayoutModule,
    InMemoryWebApiModule.forRoot(InMemoryDataService),
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
