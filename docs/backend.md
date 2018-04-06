# Data needed from the external APIs

## Location API /location

### query parameters

 - type : string (empty = any)
 - name : string (empty = any)

### Results

List of objects with following attributes:

 - name : String
 - description : String (can be empty)
 - lat : Double
 - lon : Double
 - icon\_url : String (url obviously)

Example

```json
[{
 "name" : "Yacht Club"
 "description" : "Lorem ipsum dolor sit amet"
 "lat" : 1.337
 "lon" : 3.1459
 "icon_url" : "https://foo.bar/baz.jpg"
}, 
...
]
    
```
## Routing api /route

## input (json)

Api takes a list events as json where user wants to be. Event contains following information:

- name : String
- lat : Double
- lon : Double 
- start : Datetime (yyyy-mm-dd hh:mm:ss).
- end : Datetime (yyyy-mm-dd hh:mm:ss)

## Output 

Output is a list of points

- name : String
- description : String (can be empty)
- detail\_url : (can be empty)
- type: "STAY|TRAM|BUS|WALK|TROLLEY"
- path: String (polyline) | (polyline path from the next event to this one. Empty if previous event had type STAY or this is the first one)
- lat : Double
- lon : Double 
- start : Datetime (yyyy-mm-dd hh:mm:ss).
- end : Datetime (yyyy-mm-dd hh:mm:ss).

Note: In this format the end coordinates for a transport event are found in the location of the next event.


## Storage API /storage

## input POST (json)

See output of /route API

## output GET /route/route\_id

 exactly the json stored with corresponding POST
