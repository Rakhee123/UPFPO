import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Routes, RouterModule } from '@angular/router';

import { StarterComponent } from './starter.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { ToastrModule } from 'ngx-toastr';

const routes: Routes = [
  {
    path: '',
    data: {
      title: 'Text Extraction Engine',
      urls: [
        { title: 'Home', url: '/index' },
        
      ]
    },
    component: StarterComponent
  }
];

@NgModule({
  imports: [FormsModule, CommonModule, RouterModule.forChild(routes),NgbModule,ToastrModule.forRoot()],
  declarations: [StarterComponent]
})
export class StarterModule {}
