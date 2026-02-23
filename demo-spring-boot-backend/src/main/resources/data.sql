-- One demo item per card type

-- Event Ticket (entrances)
INSERT INTO tickets (ref_id, status, title, venue, gate, reservation_number, provider_name, event_at, grouping_id, order_id, classification, holder_name, grade, barcode_value, created_at, updated_at) VALUES
('ref-ticket-001', 'ISSUED', 'Samsung Wallet Demo Concert', 'Demo Arena', 'Gate A', 'RSV-2025-001', 'Demo Provider', CURRENT_TIMESTAMP, 'group-2025-001', 'order-2025-001', 'ONETIME', 'Kim Eunha', 'Standard', 'serial-2025-001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Boarding Pass (airlines)
INSERT INTO boarding_passes (ref_id, title, provider_name, passenger_name, vehicle_number, seat_class, seat_number, reservation_number, depart_name, depart_code, depart_terminal, arrive_name, arrive_code, grouping_id, depart_gate, arrive_terminal, arrive_gate, baggage_allowance, boarding_seq_no, barcode_value, depart_at, arrive_at, created_at, updated_at) VALUES
('ref-bp-001', 'OO AIR BOARDING PASS', 'OO AIR', 'GIL DONG HONG', 'SE123', 'Economy Plus', 'A15', 'A238473-1', 'SEOUL/INCHEON', 'ICN', '2', 'SAN FRANCISCO', 'SFO', 'SE867132687321', '1', 'A', '11', '15KG', '32', 'CS16138353212584806754FG1802', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Coupon (others)
INSERT INTO coupons (ref_id, title, brand_name, expiry_at, barcode_value, created_at, updated_at) VALUES
('ref-coupon-001', '20% Off Next Purchase', 'Demo Store', DATEADD('DAY', 30, CURRENT_TIMESTAMP), 'CS16138353212584806754FG1802', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Digital ID (employees)
INSERT INTO id_cards (ref_id, title, holder_name, second_holder_name, identifier, id_number, provider_name, organization, position, expiry_at, created_at, updated_at) VALUES
('ref-id-001', 'Employee ID Card', 'Kim Samsung', 'Samsung', '2306070003', 'B19MBA115', 'Samsung Electronics', 'Digital Wallet, MX', 'Professional', DATEADD('YEAR', 2, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
