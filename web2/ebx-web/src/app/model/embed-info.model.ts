import { BaseDocumentEntity } from './base-document-entity.model';

export class EmbedInfo extends BaseDocumentEntity {
  type: string;
  url: string;
  site: string;
  title: string;
  description: string;
  images: Image[];
  videos: Video[];
  audios: Audio[];
  oembed: OEmbed;
}

export class Image {
  url: string;
  width: number;
  height: number;
}

export class Video {
  url: string;
  type: string;
}

export class Audio {
  url: string;
}

export class OEmbed {
  type: string;
  version: string;
  title: string;
  authorName: string;
  authorUrl: string;
  providerName: string;
  providerUrl: string;
  cacheAge: number;
  thumbnailUrl: string;
  thumbnailWidth: number;
  thumbnailHeight: number;
  url: string;
  html: string;
  width: number;
  height: number;
}