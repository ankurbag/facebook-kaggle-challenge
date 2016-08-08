library(data.table) #reading in the data
library(dplyr) #dataframe manipulation
library(ggplot2) #viz
library(ranger) #the random forest implementation
library(plotly) #3D plotting
library(tidyr) #dataframe manipulation
library(FNN) #k nearest neighbors algorithm
library(xgboost)

fb <- fread("C:\\fbhr.csv", integer64 = "character", showProgress = FALSE)

str(fb)
summary(fb)

## converting time assuming it is in minutes
fb$hour = (fb$time/60) %% 24
fb$weekday = (fb$time/(60*24)) %% 7
fb$month = (fb$time/(60*24*30)) %% 12 #month-ish
fb$year = fb$time/(60*24*365)
fb$day = fb$time/(60*24) %% 365


## sepearating a location
fb %>% filter(x >1, x <1.25, y >2.5, y < 2.75) -> fb
head(fb, 3)

## creating training and validation data set
small_train = fb[fb$time < 7.3e5,]
small_val = fb[fb$time >= 7.3e5,] 

##data analysis
ggplot(small_train, aes(x, y )) +
  geom_point(aes(color = place_id)) + 
  theme_minimal() +
  theme(legend.position = "none") +
  ggtitle("Check-in by place_id")

g <- ggplot(small_train, aes(x = x, y = y)) +
  stat_density(aes(fill = accuracy), geom = "polygon")
ggplotly(g)



small_train %>% count(place_id) %>% filter(n > 500) -> ids
small_trainz = small_train[small_train$place_id %in% ids$place_id,]
head(small_trainz)

plot_ly(data = small_trainz, x = x , y = y, z = hour, color = place_id, 
type = "scatter3d", mode = "markers", marker=list(size= 5)) %>% 
  layout(title = "Place_id's by position and Time of Day")



plot_ly(data = small_trainz, x = x , y = y, z = hr, color = place_id, 
type = "scatter3d", mode = "markers", marker=list(size= 5)) %>% 
  layout(title = "Place_id's by position and Time of Day")



plot_ly(data = small_trainz, x = x , y = y, z = accuracy, color = place_id,  type = "scatter3d", mode = "markers",
        marker=list(size= 5)) %>% layout(title = "Accuracy vs position coordinate ")

plot_ly(data = small_trainz, x = place_id , y = accuracy,type = "scatter", mode = "markers", marker=list(size= 5)) %>%
  layout(title = "Accuracy vs position coordinate")



p <- ggplot(small_trainz, aes(factor(accuracy), place_id))
p + geom_boxplot() + geom_jitter()

p <- plot_ly(small_trainz, x = accuracy, color = place_id, type = "box")
p


d <- plot_ly(small_trainz, x = x, y = y, text = paste("Clarity: ", hour),
             mode = "markers", color = place_id, size = 2, opacity = place_id)

p <- table(small_train$x,small_train$y,small_train$place_id)
  
p <- plot_ly(small_trainz, x = hour, y = accuracy, name = "Accuracy")
p %>% add_trace(y = fitted(loess(accuracy ~ as.numeric(accuracy))), x = hour)
