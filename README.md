# Flickr Image-Search

This application demostrates endless scrolling of images. 

Description of main classes used:

SearchActivityMain
   Main activity which display the images and allows endless scrolling to show more images
   It uses a recycler view with a grid layout manager to display these images.
EndlessRecyclerViewScrollListener: 
   Listens for scroll event in recylcer view and triggers loadNext of the pages from flickr API.
PhotoAdapter
   Adapter to the RecyclerView, uses ImageLoader to load the image

FileCache:
   Maintains a copy of the image in file system
ImageLoader
   Loads and asks for a scaled copy of the image from flickr api. It first searches in Memory Cache,
   if not found there then in File Cache and ultimately fetches the copy from network if not found
   in the first two caches.
MemoryCache
   Maintains a copy of the image in memory in a LRU cache

RestAPI
   REST API call
RestAPIFactory
   Factory to create an instance of above RestAPI


Photo
   Data Model received in first request from Flickr API
PhotoModel
   The actual photo data from flickr api, this is contained in Photo model
SearchResponse
   This is the main holder above mentioned Photo data model

Utils
   Utility class

