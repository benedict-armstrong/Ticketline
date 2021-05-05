import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {ApplicationNewsService} from '../../services/news.service';
import {News} from '../../dtos/news';
import {Event} from '../../dtos/event';
import {ApplicationEventService} from '../../services/event.service';

@Component({
  selector: 'app-add-news',
  templateUrl: './add-news.component.html',
  styleUrls: ['./add-news.component.scss']
})
export class AddNewsComponent implements OnInit {

  addNewsForm: FormGroup;
  eventId = 0;
  event = null;
  submitted = false;
  error = false;
  errorMessage = '';
  fileNoImage = false;
  success = false;

  news: News = new News(null
    , null
    , 'Moritz' // TODO add Username
    , null
    , null
    , null
    , []);

  constructor(private applicationNewsService: ApplicationNewsService, private applicationEventService: ApplicationEventService,
              private formBuilder: FormBuilder, private router: Router, private actRoute: ActivatedRoute) {
    this.eventId = this.actRoute.snapshot.params.id;
    this.loadEvent(this.eventId);

    this.addNewsForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.maxLength(100)]],
      eventName: [''],
      text: ['', [Validators.required, Validators.maxLength(10000)]],
      // event: ['', [Validators.required]],
      files: ['']
    });
  }

  ngOnInit(): void {
  }

  addNews() {
    this.submitted = true;
    if (this.addNewsForm.valid) {
      // Add additional news data
      this.news.title = this.addNewsForm.value.title;
      this.news.text = this.addNewsForm.value.text;
      this.news.event = this.event;

      this.applicationNewsService.publishNews(this.news).subscribe(
        () => {
          this.success = true;
        },
        error => {
          this.defaultServiceErrorHandling(error);
        }
      );
    } else {
      console.log('Invalid input');
      console.log(this.addNewsForm);
    }
  }

  loadEvent(id) {
    console.log('loading id:' + id);
    this.applicationEventService.getEventById(id).subscribe(
      (event: Event) => {
        this.event = event;
        this.addNewsForm.patchValue({
          eventName: event.title
        });
      },
      error => {
        this.defaultServiceErrorHandling(error);
      }
    );
  }

  onFileChange(event) {
    this.fileNoImage = false;
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      if (file.type.includes('image')) {
        this.news.picture.push(file);
        console.log(file);
      } else {
        this.fileNoImage = true;
      }
    }
  }

  removeImage(index) {
    if (index > -1) {
      this.news.picture.splice(index, 1);
    }
  }

  vanishAlert(): void {
    this.error = false;
    this.success = false;
  }



  private defaultServiceErrorHandling(error: any) {
    console.log(error);
    this.error = true;
    if (typeof error.error === 'object') {
      this.errorMessage = error.error.error;
    } else {
      this.errorMessage = error.error;
    }
  }

}
