-- Drop and recreate idx_house_deals_apt_seq
DROP INDEX IF EXISTS idx_house_deals_apt_seq ON house_deals;
CREATE INDEX idx_house_deals_apt_seq ON house_deals (apt_seq);

-- Drop and recreate idx_house_infos_apt_seq
DROP INDEX IF EXISTS idx_house_infos_apt_seq ON house_infos;
CREATE INDEX idx_house_infos_apt_seq ON house_infos (apt_seq);

-- Drop and recreate idx_house_infos_sgg_cd
DROP INDEX IF EXISTS idx_house_infos_sgg_cd ON house_infos;
CREATE INDEX idx_house_infos_sgg_cd ON house_infos (sgg_cd);

-- Drop and recreate idx_house_infos_sgg_umd
DROP INDEX IF EXISTS idx_house_infos_sgg_umd ON house_infos;
CREATE INDEX idx_house_infos_sgg_umd ON house_infos (sgg_cd, umd_cd);

-- Drop and recreate idx_house_infos_sort
DROP INDEX IF EXISTS idx_house_infos_sort ON house_infos;
CREATE INDEX idx_house_infos_sort ON house_infos (sgg_cd, umd_cd, apt_seq);

-- Drop and recreate idx_house_deals_date_sort
DROP INDEX IF EXISTS idx_house_deals_date_sort ON house_deals;
CREATE INDEX idx_house_deals_date_sort ON house_deals (deal_year, deal_month, deal_day);
