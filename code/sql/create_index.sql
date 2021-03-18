--customer
CREATE INDEX cust_id ON Customer USING btree(id);
CREATE INDEX cust_fname ON Customer USING btree(fname);
CREATE INDEX cust_lname ON Customer USING btree(lname);
CREATE INDEX cust_gender ON Customer USING btree(gtype);
CREATE INDEX cust_dob ON Customer USING btree(dob);
CREATE INDEX cust_addr ON Customer USING btree(dob);
CREATE INDEX cust_phone ON Customer USING btree(phone);
CREATE INDEX cust_zipcode ON Customer USING btree(zipcode);

--captain
CREATE INDEX cap_id ON Captain USING btree(id);
CREATE INDEX cap_fname ON Captain USING btree(fullname);
CREATE INDEX cap_nationality ON Captain USING btree(nationality);

--cruise
CREATE INDEX cru_num ON Cruise USING btree(cnum); 
CREATE INDEX cru_cost ON Cruise USING btree(cost);
CREATE INDEX cru_num_sold ON Cruise USING btree(num_sold);
CREATE INDEX cru_num_stops ON Cruise USING btree(num_stops);
CREATE INDEX cru_act_depart_date ON Cruise USING btree(actual_departure_date);
CREATE INDEX cru_act_arrival_date ON Cruise USING btree(actual_arrival_date);
CREATE INDEX cru_arrival_port ON Cruise USING btree(arrival_port);
CREATE INDEX cru_departure_port ON Cruise USING btree(departure_port);

--ship
CREATE INDEX ship_id ON Ship USING btree(id); 
CREATE INDEX ship_make ON Ship USING btree(make);
CREATE INDEX ship_model ON Ship USING btree(model);
CREATE INDEX ship_age ON Ship USING btree(age);
CREATE INDEX ship_seats ON Ship USING btree(seats);

--technician
CREATE INDEX tech_id ON Technician USING btree(id); 
CREATE INDEX tech_fname ON Technician USING btree(full_name);

--Reservation
CREATE INDEX res_rnum ON Reservation USING btree(rnum); 
CREATE INDEX res_ccid ON Reservation USING btree(ccid);
CREATE INDEX res_cid ON Reservation USING btree(cid);
CREATE INDEX res_status ON Reservation USING btree(status);

--CruiseInfo
CREATE INDEX info_ciid ON CruiseInfo Using btree(ciid); 
CREATE INDEX info_cruise_id ON CruiseInfo Using btree(cruise_id);
CREATE INDEX info_captain_id ON CruiseInfo Using btree(captain_id);
CREATE INDEX info_ship_id ON CruiseInfo Using btree(ship_id);

--Repairs
CREATE INDEX rep_rid ON Repairs Using btree(rid);
CREATE INDEX rep_date ON Repairs Using btree(repair_date);
CREATE INDEX rep_code ON Repairs Using btree(repair_code);
CREATE INDEX rep_captain_id ON Repairs Using btree (captain_id);
CREATE INDEX rep_ship_id ON Repairs USING btree(ship_id);
CREATE INDEX rep_technician_id ON Repairs USING btree(technician_id);

--Schedule
CREATE INDEX sche_id ON Schedule USING btree(id);
CREATE INDEX sche_cruiseNum ON Schedule USING btree(cruiseNum);
CREATE INDEX sche_departure_time ON Schedule USING btree(departure_time);
CREATE INDEX sche_arrival_time ON Schedule USING btree(arrival_time);
