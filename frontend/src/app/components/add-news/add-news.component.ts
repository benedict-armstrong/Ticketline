import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {ApplicationNewsService} from '../../services/news.service';
import {News} from '../../dtos/news';
import {Event} from '../../dtos/event';

@Component({
  selector: 'app-add-news',
  templateUrl: './add-news.component.html',
  styleUrls: ['./add-news.component.scss']
})
export class AddNewsComponent implements OnInit {

  addNewsForm: FormGroup;
  submitted = false;
  error = false;
  errorMessage = '';
  fileNoImage = false;
  success = false;

  news: News = new News(null
    , null
    , null // TODO add Username
    , null
    , null
    , null
    , []);

  constructor(private applicationNewsService: ApplicationNewsService, private formBuilder: FormBuilder, private router: Router) {
    this.addNewsForm = this.formBuilder.group({
      title: ['', [Validators.required, Validators.maxLength(10)]],
      text: ['', [Validators.required, Validators.maxLength(10000)]],
      event: ['', [Validators.required]],
      files: ['']
    });
  }

  addNews() {
    this.submitted = true;
    if (this.addNewsForm.valid) {
      // Add additional news data
      this.news.title = this.addNewsForm.value.title;
      this.news.text = this.addNewsForm.value.text;
      this.news.event = new Event(this.addNewsForm.value.event.value
        // TODO Add other Event data
      );

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

  ngOnInit(): void {
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
