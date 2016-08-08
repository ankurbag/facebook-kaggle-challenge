library(data.table) #reading in the data
library(dplyr) #dataframe manipulation
library(ggplot2) #viz
library(ranger) #the random forest implementation
library(plotly) #3D plotting
library(tidyr) #dataframe manipulation
library(FNN) #k nearest neighbors algorithm
library(xgboost)
library(data.table)

fb <- fread("D:\\Big Data\\FacebookData\\train1.csv\\train.csv", integer64 = "character", showProgress = FALSE)


fb %>% filter(x =1, x <1.25, y >2.5, y < 2.75) -> fb
head(fb, 3)
## timestamp conversion

fb$hour = (fb$time/60) %% 24
fb$weekday = (fb$time/(60*24)) %% 7
fb$month = (fb$time/(60*24*30)) %% 12 #month-ish
fb$year = fb$time/(60*24*365)
fb$day = fb$time/(60*24) %% 365

## creating training and validation set

small_train = fb[fb$time < 7.3e5,]
small_val = fb[fb$time >= 7.3e5,] 


small_train %>% count(place_id) %>% filter(n > 3) -> ids
small_train = small_train[small_train$place_id %in% ids$place_id,]


s = 2
l = 125
w = 500

create_matrix = function(train) {
  cbind(s*train$y,
        train$x,
        train$hour/l,
        train$weekday/w,
        train$year/w,
        train$month/w,
        train$time/(w*60*24*7))
}

X = create_matrix(small_train)
X_val = create_matrix(small_val)
X

model_knn = FNN::knn(train = X, test = X_val, cl = small_train$place_id, k = 15)


model_knn

preds <- as.character(model_knn)
truth <- as.character(small_val$place_id)

mean(truth == preds)


##########################################################Test 1

fb1 <- fread("D:\\Big Data\\FacebookData\\train1.csv\\train.csv", integer64 = "character", showProgress = FALSE)


fb1 %>% filter(x =1, x <2, y >1, y < 2) -> fb1
head(fb1, 3)

fb1$hour = (fb1$time/60) %% 24
fb1$weekday = (fb1$time/(60*24)) %% 7
fb1$month = (fb1$time/(60*24*30)) %% 12 #month-ish
fb1$year = fb1$time/(60*24*365)
fb1$day = fb1$time/(60*24) %% 365


small_train1 = fb1[fb1$time < 7.3e5,]
small_val1 = fb1[fb1$time >= 7.3e5,] 


small_train1 %>% count(place_id) %>% filter(n > 3) -> ids
small_train1 = small_train1[small_train1$place_id %in% ids$place_id,]


small_train1

s = 2
l = 125
w = 500

create_matrix = function(train) {
  cbind(s*train1$y,
        train1$x,
        train1$hour/l,
        train1$weekday/w,
        train1$year/w,
        train1$month/w,
        train1$time/(w*60*24*7))
}

X1 = create_matrix(small_train1)
X_val1 = create_matrix(small_val1)
X1

model_knn = FNN::knn(train = X1, test = X_val1, cl = small_train1$place_id, k = 15)


model_knn

preds <- as.character(model_knn)
truth <- as.character(small_val1$place_id)

mean(truth == preds)

##########################################################Test 2

fb <- fread("D:\\Big Data\\FacebookData\\train1.csv\\train.csv", integer64 = "character", showProgress = FALSE)

fb2 %>% filter(x =1, x <1.25, y >2.5, y < 2.75) -> fb2
#########
val <- 1352068320
as.POSIXct(val, origin="1970-01-01")
as.Date(as.POSIXct(val, origin="1970-01-01"))
d <-as.POSIXct(val, origin="1970-01-01")
format(d,"%H")
format(d,"%I")
#############

## converting time to epoch time
fb$hr <- as.numeric(format(as.POSIXct(fb$time, origin="1970-01-01"),"%H"))

write.csv(fb, "fbhr.csv", row.names=FALSE, quote=FALSE)

dim(fb2)

fb2$Sh1 <- 0
fb2$Sh2 <- 0
fb2$Sh3 <- 0



fb2$hr >2
fb2$hr<=8 
all.(fb2$Sh1,8)
dim(fb2)
n.fb2

fb2$Sh1<-ifelse((fb2$hr<=8),1, 0)

fb2$Sh2<-ifelse((fb2$hr>=8 & fb2$hr<16),1, 0)
fb2$Sh3<-ifelse((fb2$hr>16),1, 0)

small_trainfb = fb2[fb2$time < 7.3e5,]
small_valfb = fb2[fb2$time >= 7.3e5,] 

small_train

s = 2
l = 125
w = 500

create_matrix = function(train) {
  cbind(train$y,
        train$x,
        train$hr,
        train$Sh1,
        train$Sh2,
        train$Sh3
        )
}

X1 = create_matrix(small_trainfb)
X_val1 = create_matrix(small_valfb)
X1
X_val1

model_knn = FNN::knn(train = X1, test = X_val1, cl = small_trainfb$place_id, k = 15)


model_knn

preds <- as.character(model_knn)
truth <- as.character(small_valfb$place_id)

preds
truth

mean(truth == preds)


str(fb2)

table(fb2$hr)

round(prop.table(table(fb2$hr)) * 100, digits = 1)
summary(fb2)
CrossTable(x = X_val1, y = X_val1, prop.chisq=FALSE)