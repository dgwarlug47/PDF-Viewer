library(wooldridge)
data(wage2)
dat <- wage2

Wm <- dat$wage[dat$married==1]
Wnm <- dat$wage[dat$married==0]
Wh <- dat$wage[dat$KWW>40]
Wl <- dat$wage[dat$KWW<30]

#a
print(paste("average married worker wage: ", as.character(mean(Wm))))
print(paste("average non married worker wage: ", as.character(mean(Wnm))))
print(paste("average high KWW score worker wage: ", as.character(mean(Wh))))
print(paste("average low KWW score worker wage: ", as.character(mean(Wl))))

#b
print(paste("std married worker wage: ", as.character(sd(Wm))))
print(paste("std non married worker wage: ", as.character(sd(Wnm))))
print(paste("std high KWW score worker wage: ", as.character(sd(Wh))))
print(paste("std low KWW score worker wage: ", as.character(sd(Wl))))

#c
filtered_data <- dat[,c("wage", "hours", "IQ", "KWW", "educ")]

filtered_data_Wm <- subset(filtered_data, married==1)
filtered_data_Wnm <- subset(filtered_data, married==0)
filtered_data_Wh <- subset(filtered_data, KWW>40)
filtered_data_Wl <- subset(filtered_data, KWW<30)

library(stargazer)
stargazer(filtered_data_Wm, type="text")
stargazer(filtered_data_Wnm, type="text")
stargazer(filtered_data_Wh, type="text")
stargazer(filtered_data_Wl, type="text")

#d

samples_and_normal <- function(samples, title) {
  hist(samples, main=title, freq=F, breaks=100)
  curve(dnorm(x, mean(samples), sd(samples)), min(samples), max(samples), add=TRUE, color="violet")
}

samples_and_normal(Wm, "married workers")

samples_and_normal(Wnm, "non married workers")

samples_and_normal(Wh, "high KWW score workers")

samples_and_normal(Wl, "low KWW score workers")

#e

log_Wm <- log(Wm)
log_Wnm <- log(Wnm)
log_Wh <- log(Wh)
log_Wl <- log(Wl)


samples_and_normal(log_Wm, "log married workers")

samples_and_normal(log_Wnm, "log non married workers")

samples_and_normal(log_Wh, "log high KWW score workers")

samples_and_normal(log_Wl, "log low KWW score workers")

#f

qqnorm(Wm, main="qqplot of married workers")
qqline(Wm)

qqnorm(Wnm, main="qqplot of non married workers")
qqline(Wnm)

qqnorm(Wh, main="qqplot of high KWW score workers")
qqline(Wh)

qqnorm(Wl, main="qqplot of low KWW score workers")
qqline(Wl)

qqnorm(log_Wm, main="qqplot of log married workers")
qqline(log_Wm)

qqnorm(log_Wnm, main="qqplot of log non married workers")
qqline(log_Wnm)

qqnorm(log_Wh, main="qqplot of log high KWW score workers")
qqline(log_Wh)

qqnorm(log_Wl, main="qqplot of log low KWW score workers")
qqline(log_Wl)

#g

boxplot(dat$wage~dat$married,xlabel="", main="boxplot of wage as a function of the marital status", xlabel="married", ylabel="wage")

