import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ArtistService} from '../../../services/artist.service';
import {Artist} from '../../../dtos/artist';

@Component({
  selector: 'app-artist-search',
  templateUrl: './artist-search.component.html',
  styleUrls: ['./artist-search.component.scss']
})
export class ArtistSearchComponent implements OnInit {
  @Output() searchedArtists = new EventEmitter<Artist[]>();
  @Output() searchedNoArtist = new EventEmitter<any>();

  // Error flag
  error = false;
  errorMessage = '';

  artistSearchForm: FormGroup;
  page = 0;
  size = 8;
  artists = [];
  noArtist = true;

  constructor(private formBuilder: FormBuilder, private artistService: ArtistService) { }

  ngOnInit(): void {

    this.artistSearchForm = this.formBuilder.group({
      firstName: ['', []],
      lastName: ['', []]
    });
  }

  searchArtists() {
    if (this.noArtist && this.page !== 0) {
      return;
    }

    if (this.artistSearchForm.value.firstName === '' && this.artistSearchForm.value.lastName === '') {
      this.artistService.getArtists(this.page, this.size).subscribe(
        response => {
          const nonEmptyArtist = response.filter((artist) => (artist.performances.length !== 0));

          this.artists.push(...nonEmptyArtist);
          if (nonEmptyArtist.length < this.size) {
            this.noArtist = true;
          } else {
            this.page++;
            this.noArtist = false;
          }

          this.searchedArtists.emit(this.artists);
          this.searchedNoArtist.emit(this.noArtist);
        }, error => {
          console.error(error);
        }
      );
    } else {
      this.artistService.searchArtists(this.page, this.size, this.artistSearchForm.value.firstName,
        this.artistSearchForm.value.lastName).subscribe(
        response => {
          this.artists.push(...response);
          if (response.length < this.size) {
            this.noArtist = true;
          } else {
            this.page++;
            this.noArtist = false;
          }
          this.searchedArtists.emit(this.artists);
          this.searchedNoArtist.emit(this.noArtist);
        }, error => {
          console.error(error);
        }
      );
    }
  }

  resetValues() {
    this.artists = [];
    this.noArtist = true;
    this.page = 0;
    this.size = 8;
  }
}
