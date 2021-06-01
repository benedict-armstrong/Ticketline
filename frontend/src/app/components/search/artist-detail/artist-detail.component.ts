import { Component, OnInit } from '@angular/core';
import {Performance} from '../../../dtos/performance';
import {ApplicationPerformanceService} from '../../../services/performance.service';
import {ActivatedRoute} from '@angular/router';
import {Artist} from '../../../dtos/artist';
import {ArtistService} from '../../../services/artist.service';

@Component({
  selector: 'app-artist-detail',
  templateUrl: './artist-detail.component.html',
  styleUrls: ['./artist-detail.component.scss']
})
export class ArtistDetailComponent implements OnInit {

  artist: Artist;
  performances: Performance[] = [];
  page = 0;
  size = 8;
  noPerformance = false;

  constructor(private performanceService: ApplicationPerformanceService,
              private activeRoute: ActivatedRoute, private artistService: ArtistService) { }

  ngOnInit(): void {
    const artistId = this.activeRoute.snapshot.params.id;

    this.artistService.getArtistById(artistId).subscribe(response => {
      this.artist = response;
      this.getPerformancesForArtist();
    });
  }

  getPerformancesForArtist(): void {
    this.performanceService.getPerformancesForArtistId(this.artist.id, this.page, this.size).subscribe(
      response => {
        this.performances.push(...response);
        if (response.length < this.size) {
          this.noPerformance = true;
        } else {
          this.page++;
          this.noPerformance = false;
        }
      }, error => {
        console.error(error);
      }
    );
  }
}
