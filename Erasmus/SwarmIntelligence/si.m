clear all;
close all;

global map;
global map_size;
global boid_size;
global boids_num;
global boids;

boids_num = 20;
map_size = 300;
boid_size = 5;
map = false(map_size,map_size);
collision_offset = 15; 
velocityLimit = 10; % 0 - no limit

it = 1;
while(it<=boids_num)
    x = floor(rand()*map_size + 1);
    y = floor(rand()*map_size + 1);
    if(checkColision(x,y))
        setBoid(x,y,1);
        boids(it).p = [x y];
        boids(it).v = [(rand()*20-10) (rand()*20-10)];
        it=it+1;
    end
end

figure,
imshow(map);
while(1)
    for i = 1:boids_num
        v = boids(i).v;
        p = boids(i).p;
        
        clearvars nearests;
        for j = 1:boids_num
            dist  = sqrt(sum((p - boids(j).p) .^ 2));
            nearests(j,1) = dist;
            nearests(j,2) = j;
            
        end
        nearests = sortrows(nearests,1);
        
        %rule 1
        r1 = [0 0];
        for j = 2:6 % 5 nearest boids
            r1 = r1 + boids(nearests(j,2)).p;
        end
        r1 = r1 / 5;
        r1 = r1 - p;
        %rule 1
        
        %rule 2
        r2 = [0 0];
        for j = 2:boids_num
            if(nearests(j,1) < collision_offset)
                r2 = r2 - (boids(nearests(j,2)).p - p);
            else
                break;
            end
        end
        if(p(1) > (map_size - collision_offset))
            r2(1) = r2(1) - (map_size - p(1));
        end
        if(p(2) > (map_size - collision_offset))
            r2(2) = r2(2) - (map_size - p(2));
        end
        if(p(1) < collision_offset)
            r2(1) = r2(1) - (0 - p(1));
        end
        if(p(2) < collision_offset)
            r2(2) = r2(2) - (0 - p(2));
        end
        %rule 2
        
        %rule 3
        r3 = [0 0];
        for j = 2:6 % 5 nearest boids
            r3 = r3 + boids(nearests(j,2)).v;
        end
        r3 = r3 / 5;
        r3 = r3 - v;
        %rule 3
        
        %constants
        a = 1;
        b = 0.02;
        c = 0.3;
        d = 0.12;
        
        boids(i).v = (v * a) + (r1 * b) + (r2 * c) + (r3 * d);
        
        if(velocityLimit > 0)
            if(boids(i).v(1) > velocityLimit )
                boids(i).v(1) = velocityLimit;
            end
            if(boids(i).v(1) < -velocityLimit )
                boids(i).v(1) = -velocityLimit;
            end
            if(boids(i).v(2) > velocityLimit )
                boids(i).v(2) = velocityLimit;
            end
            if(boids(i).v(2) < -velocityLimit )
                boids(i).v(2) = -velocityLimit;
            end
        end
        
        moved = 0;
        while(not(moved))
            target = floor(p + boids(i).v);
            setBoid(p(1),p(2),0);
            if(checkColision(target(1),target(2))) % no collision
                setBoid(target(1),target(2),1);
                boids(i).p = target;
                moved = 1;
            else % collision detected slowing down until movement can be performed
                boids(i).v = boids(i).v/2;
            end
        end
    end
    imshow(map);
end


