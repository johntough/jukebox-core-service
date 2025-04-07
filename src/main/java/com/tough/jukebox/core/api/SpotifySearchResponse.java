package com.tough.jukebox.core.api;

import java.util.List;

public class SpotifySearchResponse {
    private Artists artists;

    public Artists getArtists() {
        return artists;
    }

    public void setArtists(Artists artists) {
        this.artists = artists;
    }

    public static class Artists {
        private List<Item> items;

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public static class Item {
            private String id;
            private String name;
            private Followers followers;
            private List<String> genres;
            private List<Image> images;
            private ExternalUrls externalUrls;
            private int popularity;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Followers getFollowers() {
                return followers;
            }

            public void setFollowers(Followers followers) {
                this.followers = followers;
            }

            public List<String> getGenres() {
                return genres;
            }

            public void setGenres(List<String> genres) {
                this.genres = genres;
            }

            public List<Image> getImages() {
                return images;
            }

            public void setImages(List<Image> images) {
                this.images = images;
            }

            public ExternalUrls getExternalUrls() {
                return externalUrls;
            }

            public void setExternalUrls(ExternalUrls externalUrls) {
                this.externalUrls = externalUrls;
            }

            public int getPopularity() {
                return popularity;
            }

            public void setPopularity(int popularity) {
                this.popularity = popularity;
            }
        }

        public static class Followers {
            private int total;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }
        }

        public static class Image {
            private String url;
            private int height;
            private int width;

            // Getters and setters
            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }
        }

        public static class ExternalUrls {
            private String spotify;

            public String getSpotify() {
                return spotify;
            }

            public void setSpotify(String spotify) {
                this.spotify = spotify;
            }
        }
    }
}

