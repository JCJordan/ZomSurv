%% load data

names = {'roadLocal.png';'roadArterial.png';'roadHighway.png';'water.png';'railway.png';'parks.png';'business.png';'medical.png';'school.png';'sports.png'};

data = zeros(640,640,numel(names),'logical');
for i = 1:numel(names)
    temp = imread(names{i});
    data(:,:,i) = temp(:,:,1) ~= 255;
end


%% make accessibility

accessibility = zeros(640,640,'uint8');

accessibility = uint8(any(data(:,:,1:3),3))*255;

accessibility = bwareaopen(accessibility,1000);

%% save
imwrite(accessibility,'accessibility.png');

%% create map with upscaled values

clear map;
for i = 1:3
    map(:,:,i) = imresize(data(:,:,i),4,'lanczos2');
    map(:,:,i) = map(:,:,i)>=1;
end

thickness = [4,8,12];
for i = 1:3
    map(:,:,i) = bwmorph(map(:,:,i),'thin',inf);
    map(:,:,i) = imdilate(map(:,:,i),strel('disk',thickness(i)));
end

map = any(map,3);
map = bwareaopen(map,16000);

imshow(map);


%% save
imwrite(map,'map.png');


%% population density

temp = imread('sectors.png');
temp = imdilate(temp,strel('disk',2));
sectors = zeros(size(temp),'uint8');

sectors(temp<20) = 1;
sectors(and(temp>=20, temp<50)) = 2;
sectors(and(temp>=50, temp<90)) = 3;
sectors(and(temp>=90, temp<125)) = 4;
sectors(and(temp>=125, temp<175)) = 5;
sectors(and(temp>=175, temp<255)) = 6;

density = [3.8, 9.7, 20, 11.0, 6.5, 16.2] * 1000;
population = zeros(size(sectors),'uint16');
for i = 1:640
    for j = 1:640
        population(i,j) = uint16(round(density(sectors(i,j))*(rand/2+0.5) * accessibility(i,j)));
    end
end

%% save
imwrite(population,'population.png');