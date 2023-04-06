package com.nucleus.floracestore.init;

import com.nucleus.floracestore.error.QueryRuntimeException;
import com.nucleus.floracestore.model.entity.*;
import com.nucleus.floracestore.model.enums.CountryEnum;
import com.nucleus.floracestore.model.enums.OrderStatusCodes;
import com.nucleus.floracestore.model.enums.PhonePrefixesEnum;
import com.nucleus.floracestore.model.enums.ProductStatusEnum;
import com.nucleus.floracestore.repository.*;
import com.nucleus.floracestore.service.OrderItemsStatusCodesService;
import com.nucleus.floracestore.service.OrderStatusCodesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;
    private final PhonePrefixRepository phonePrefixRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductSubCategoryRepository productSubCategoryRepository;
    private final ProductStatusRepository productStatusRepository;
    private final ProductRepository productRepository;
    private final StorageRepository storageRepository;
    private final ResourceLoader resourceLoader;
    private final Path fileStorageLocation;
    private final OrderStatusCodesService orderStatusCodesService;

    private final OrderItemsStatusCodesService orderItemsStatusCodesService;

    @Autowired
    public SetupDataLoader(CountryRepository countryRepository, UserRepository userRepository,
                           RoleRepository roleRepository,
                           PrivilegeRepository privilegeRepository,
                           PasswordEncoder passwordEncoder,
                           PhonePrefixRepository phonePrefixRepository, ProductCategoryRepository productCategoryRepository,
                           ProductSubCategoryRepository productSubCategoryRepository,
                           ProductStatusRepository productStatusRepository,
                           ProductRepository productRepository,
                           StorageRepository storageRepository,
                           ResourceLoader resourceLoader,
                           Environment environment,
                           OrderStatusCodesService orderStatusCodesService,
                           OrderItemsStatusCodesService orderItemsStatusCodesService) throws IOException, InterruptedException {
        this.countryRepository = countryRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
        this.phonePrefixRepository = phonePrefixRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productSubCategoryRepository = productSubCategoryRepository;
        this.productStatusRepository = productStatusRepository;
        this.productRepository = productRepository;
        this.storageRepository = storageRepository;
        this.resourceLoader = resourceLoader;
        this.fileStorageLocation = Paths.get(environment.getProperty("app.file.upload-dir", "./src/main/resources/static/images/uploads"));
        this.orderStatusCodesService = orderStatusCodesService;
        this.orderItemsStatusCodesService = orderItemsStatusCodesService;
    }


    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (userRepository.findAll().isEmpty()) {
            if (alreadySetup)
                return;
            try {
                initializeStorage();
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }

            PrivilegeEntity readPrivilege
                    = createPrivilegeIfNotFound("READ_PRIVILEGE");
            PrivilegeEntity writePrivilege
                    = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
            PrivilegeEntity updatePrivilege
                    = createPrivilegeIfNotFound("UPDATE_PRIVILEGE");
            PrivilegeEntity deletePrivilege
                    = createPrivilegeIfNotFound("DELETE_PRIVILEGE");

            Set<PrivilegeEntity> adminPrivileges = Set.of(
                    readPrivilege, writePrivilege, updatePrivilege, deletePrivilege);

            Set<PrivilegeEntity> staffPrivileges = Set.of(
                    readPrivilege, writePrivilege, updatePrivilege);

            Set<PrivilegeEntity> userPrivileges = Set.of(
                    readPrivilege, writePrivilege);

            Set<PrivilegeEntity> guestPrivileges = Set.of(
                    readPrivilege, writePrivilege, updatePrivilege, deletePrivilege);

            createRoleIfNotFound("ADMIN", adminPrivileges);
            createRoleIfNotFound("STAFF", staffPrivileges);
            createRoleIfNotFound("USER", userPrivileges);
            createRoleIfNotFound("GUEST", guestPrivileges);

            RoleEntity adminRole = roleRepository.findByRoleName("ADMIN");
            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setEmail("test@test.com");
            admin.setPassword(passwordEncoder.encode("hektor123!@#"));
            admin.setAccountCreatedDate(new Date(System.currentTimeMillis()));
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);

            RoleEntity staffRole = roleRepository.findByRoleName("STAFF");
            UserEntity staff = new UserEntity();
            staff.setUsername("kiro");
            staff.setEmail("test123@test.com");
            staff.setPassword(passwordEncoder.encode("lorenzo123!@#"));
            staff.setAccountCreatedDate(new Date(System.currentTimeMillis()));
            staff.setRoles(Set.of(staffRole));
            userRepository.save(staff);

            RoleEntity userRole = roleRepository.findByRoleName("USER");
            UserEntity user = new UserEntity();
            user.setUsername("user");
            user.setEmail("test321@test.com");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setAccountCreatedDate(new Date(System.currentTimeMillis()));
            user.setRoles(Set.of(userRole));
            userRepository.save(user);

            CountryEnum[] countryCodes = CountryEnum.values();
            Arrays.stream(countryCodes).forEach(country -> {
                CountryEntity countryEntity = new CountryEntity();
                countryEntity.setCountryName(country.getCountryName());
                countryEntity.setCountryCode(country.name());
                countryRepository.save(countryEntity);
            });

            ProductStatusEnum[] productStatusEnumList = ProductStatusEnum.values();
            Arrays.stream(productStatusEnumList).forEach(status -> {
                ProductStatusEntity productStatusEntity = new ProductStatusEntity();
                productStatusEntity.setProductStatusName(status.getLevelName());
                productStatusEntity.setProductStatusDescription(status.getLevelDescription());
                productStatusRepository.save(productStatusEntity);
            });

            PhonePrefixesEnum[] phonePrefixesEnumList = PhonePrefixesEnum.values();
            Arrays.stream(phonePrefixesEnumList).forEach(status -> {
                PhonePrefixEntity phonePrefixEntity = new PhonePrefixEntity();
                phonePrefixEntity.setCountryName(status.name());
                phonePrefixEntity.setPrefix(status.getPrefix());
                phonePrefixRepository.save(phonePrefixEntity);
            });

            ProductCategoryEntity flowers = createProductCategoryIfNotFound("Flowers", "Different types of flowers arranged as bouquets, in a box, in pots.");
            ProductCategoryEntity Plants = createProductCategoryIfNotFound("Plants", "Different types of plants for the home and garden.");
            ProductCategoryEntity gifts = createProductCategoryIfNotFound("Gifts", "Creative gift solutions made by hand according to customer specific requirements.");
            ProductCategoryEntity special = createProductCategoryIfNotFound("Special", "Boutique products suitable for special occasions.");
            ProductCategoryEntity prints = createProductCategoryIfNotFound("Prints", "Printing on various surfaces.");
            ProductCategoryEntity promotions = createProductCategoryIfNotFound("Promotions", "Special offers at a super price.");

            ProductSubCategoryEntity alstroemeria = createProductSubCategoryIfNotFound("Alstroemeria"
                    , "Also known as Peruvian lily, alstroemeria is a favorite perennial for both the garden and as a cut flower with a very long life in the vase. It blooms profusely and continuously. One of the few varieties of Alstroemeria that can overwinter in the garden in our climate. Good mulching is recommended in autumn. Very beautiful large flowers in tangerine orange. A perennial that will brighten your summer garden with color. Vis. 100 cm. Not much care and requires growing in any soil."
                    , flowers);
            ProductSubCategoryEntity freesias = createProductSubCategoryIfNotFound("Freesias"
                    , "Freesias have exotic-looking blooms, which are loved for their strong spicy-sweet fragrance. Funnel-shaped, freesia flowers are borne in dense, branching clusters on slender stems 30-60 cm high, above long narrow green leaves, and come in a wonderful range of colours including yellow, mauve, purple, orange, and white. They're immensely popular as a cut flower."
                    , flowers);
            ProductSubCategoryEntity lisianthus = createProductSubCategoryIfNotFound("Lisianthus"
                    , "Lisianthus has long been grown as a cut flower, prized for its striking, rose-like beauty, wide range of colors — purple, rose, blush, white, green, cream, and various bicolors — and exceptional vase life. Lisianthus can be exacting in its culture, but with a little patience and attention to temperature and water control, you can produce a reliable supply of premium-quality blooms. Follow our Guide to Growing Lisianthus to achieve a successful, season-long harvest."
                    , flowers);
            ProductSubCategoryEntity sunflower = createProductSubCategoryIfNotFound("Sunflowers"
                    , "Sunflowers symbolise loyalty, adoration thanks to the myth of Clytie and Apollo. And, because of their association with the sun, sunflowers are well-known for being a happy flower and the perfect bloom for a summer flower delivery to brighten someone's mood!"
                    , flowers);
            ProductSubCategoryEntity lilies = createProductSubCategoryIfNotFound("Lilies"
                    , "Lilies are a beautiful flower, there is no denying that. They have a way of expressing love, friendship and care that is so beautiful. Recently From You Flowers Facebook friends said that they would rather receive a lily arrangement then a rose bouquet because lilies are more unique. Lily flowers come in a variety of colors from pink to orange and yellow. We offer flower bouquets with every lily color you could imagine. Learn what each color means at From You Flowers flower resource. And remember to shop our send flowers to find the perfect gift to send for the upcoming holiday season."
                    , flowers);
            ProductSubCategoryEntity roses = createProductSubCategoryIfNotFound("Roses"
                    , "A rose is either a woody perennial flowering plant of the genus Rosa, in the family Rosaceae, or the flower it bears. There are over three hundred species and tens of thousands of cultivars."
                    , flowers);
            ProductSubCategoryEntity tulips = createProductSubCategoryIfNotFound("Tulips"
                    , "Tulips are a genus of spring-blooming perennial herbaceous bulbiferous geophytes. The flowers are usually large, showy and brightly coloured, generally red, pink, yellow, or white. They often have a different coloured blotch at the base of the tepals, internally."
                    , flowers);
            ProductStatusEntity productStatus = productStatusRepository.findByProductStatusName("In stock").orElseThrow(() -> new QueryRuntimeException("Could not find product status with name 'IN_STOCK'"));
            createProductIfNotFound("Alstroemeria",
                    5,
                    33.99,
                    22,
                    15,
                    "White,Red,Orange",
                    "5,6,14",
                    " With their trumpet-like appearance, they resemble lilies and grow to one to three feet tall.",
                    "The alstroemeria, also called the Peruvian lily, features streaked or speckled blossoms in a range of colors, including white, yellow, orange, pink, and red.",
                    productStatus,
                    flowers,
                    alstroemeria,
                    storageRepository.findById(1L).get(),
                    userRepository.findByUsername("admin").get());
            createProductIfNotFound("Calla Lily",
                    5,
                    23.99,
                    11.44,
                    10,
                    "White,Red",
                    "5,6,14",
                    "They feature a beautiful trumpet shape with smooth, sword-like foliage.",
                    "Calla lilies come in a variety of different colors, from snow white to bright pink. ",
                    productStatus,
                    flowers,
                    lilies,
                    storageRepository.findById(2L).get(),
                    userRepository.findByUsername("admin").get());
            createProductIfNotFound("Freesias",
                    4,
                    16.99,
                    11.44,
                    7,
                    "White",
                    "5,6,14",
                    "Freesias are among the world’s most popular cut flowers. They are loved for their pure colors, long vase life and sweet perfume. ",
                    "This delicate and fragrant flower is a real favourite. Due to their citrus scent and bright colours as well as a flower meaning which signifies “friendship”, Flourish recommends the pretty Freesia bouquet as the perfect gift for all occasions.",
                    productStatus,
                    flowers,
                    freesias,
                    storageRepository.findById(3L).get(),
                    userRepository.findByUsername("admin").get());
            createProductIfNotFound("Lisianthus",
                    4,
                    56.99,
                    34.99,
                    6,
                    "White",
                    "5,6,14",
                    "Lisianthus are a soft, sweet and delicate flower from North America. The flower has a powerful meaning of gratitude, appreciation and charisma.",
                    "The lisianthus is versatile. It comes in white, green, blue, pink, lilac, purple and salmon. Some flowers even have petals with multiple colours. The petals are very soft and delicate, so make sure to handle with care.",
                    productStatus,
                    flowers,
                    lisianthus,
                    storageRepository.findById(4L).get(),
                    userRepository.findByUsername("admin").get());
            createProductIfNotFound("Sunflower",
                    5,
                    15.99,
                    13.99,
                    3,
                    "White, Pink",
                    "5,6,14",
                    "Send sunflowers for delivery in mason jars, or mixed bouquets of sunflowers and roses!",
                    "“What a bright and cheery product!” Sunflowers make everyone smile and this bouquet, which also features delightful Golden Ambition Roses and Opus Yellow Snapdragons, certainly aims to do just that.",
                    productStatus,
                    flowers,
                    sunflower,
                    storageRepository.findById(5L).get(),
                    userRepository.findByUsername("admin").get());
            createProductIfNotFound("Roses",
                    2,
                    5.99,
                    1.99,
                    3,
                    "White, Pink",
                    "5,6,14",
                    "Send a rose bouquet to your family and loved ones for any occasion.",
                    "Choose this beautiful long stemmed red rose bouquet for that special person in your life.  These beautiful Grand Prix roses really are the flower of love.  The deep red colours of the flowers look gorgeous alongside the green eucalyptus and asparagus fern foliage, making a fabulous Valentines gift.",
                    productStatus,
                    flowers,
                    roses,
                    storageRepository.findById(6L).get(),
                    userRepository.findByUsername("admin").get());
            createProductIfNotFound("Tulips",
                    2,
                    5.99,
                    1.99,
                    3,
                    "White, Pink",
                    "5,6,14",
                    "Send a Tulips bouquet to your family and loved ones for any occasion.",
                    "DOUBLE PRINCE LILAC – Tulips bring a smile to everyone’s face.  A bright and cheery flower that always keeps on growing.  The petals of the double tulips and parrot tulips are exceptionally beautiful.  A great arrangement to have in the kitchen or drawing room.",
                    productStatus,
                    flowers,
                    tulips,
                    storageRepository.findById(7L).get(),
                    userRepository.findByUsername("admin").get());

            OrderStatusCodes[] values = OrderStatusCodes.values();
            Arrays.stream(values).forEach(orderStatusCodesService::initializeOrderStatusCodesFromEnum);
            ProductStatusEnum[] orderStatusCodes = ProductStatusEnum.values();
            Arrays.stream(orderStatusCodes).forEach(orderItemsStatusCodesService::createOrderItemStatus);
            alreadySetup = true;
        }
    }

    @Transactional
    PrivilegeEntity createPrivilegeIfNotFound(String name) {
        PrivilegeEntity privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new PrivilegeEntity();
            privilege.setName(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    void createRoleIfNotFound(String name, Set<PrivilegeEntity> privileges) {
        RoleEntity role = roleRepository.findByRoleName(name);
        if (role == null) {
            role = new RoleEntity();
            role.setRoleName(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
    }

    @Transactional
    private void createProductIfNotFound(String productName,
                                         double unitQuantity,
                                         double productPrice,
                                         double unitOrderPrice,
                                         double unitDiscount,
                                         String productColor,
                                         String productSize,
                                         String productDetails,
                                         String productDescription,
                                         ProductStatusEntity productStatus,
                                         ProductCategoryEntity productCategory,
                                         ProductSubCategoryEntity productSubCategory,
                                         StorageEntity storages,
                                         UserEntity user) {
        ProductEntity product = productRepository.findByProductName(productName).orElse(null);
        if (product == null) {
            product = new ProductEntity();
            product.setProductName(productName);
            product.setUnitQuantity(new BigDecimal(unitQuantity));
            product.setUnitSellPrice(new BigDecimal(productPrice));
            product.setUnitOrderPrice(new BigDecimal(unitOrderPrice));
            product.setUnitDiscount(new BigDecimal(unitDiscount));
            product.setProductColor(productColor);
            product.setProductSize(productSize);
            product.setOtherProductDetails(productDetails);
            product.setProductDescription(productDescription);
            product.setProductStatus(productStatus);
            product.setProductCategory(productCategory);
            product.setProductSubCategory(productSubCategory);
            product.setStorages(Set.of(storages));
            product.setUser(user);
            productRepository.save(product);
        }
    }

    @Transactional
    public void initializeStorage() throws IOException, InterruptedException {
        Resource[] resources = loadResources("classpath*:/static/images/uploads/*.jpg");
        for (Resource r : resources
        ) {
            String[] arrOfStr = r.getURL().toString().split("static", 0);
            System.out.println("File path: " + arrOfStr[1]);
            String resourceType = r.getFilename().split("\\.", 0)[1];
            System.out.println(resourceType);
            StorageEntity storageEntity = new StorageEntity();
            storageEntity.setFileName(r.getFilename());
//            storageEntity.setFileUrl(arrOfStr[1]);
            storageEntity.setFileUrl("http://localhost:8080" + arrOfStr[1]);
            if (r.getURI().getPath().contains("/C:")) {
                String resultPath = r.getURI().getPath().replace("/C:", "");
                storageEntity.setSize(Files.size(Path.of(resultPath)));
            } else {
                storageEntity.setSize(Files.size(Path.of(r.getURI().getPath())));
            }
            storageRepository.save(storageEntity);
        }
        Thread.sleep(3000);

    }

    public Resource[] loadResources(String pattern) throws IOException {
        return ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(pattern);
    }

    @Transactional
    private ProductCategoryEntity createProductCategoryIfNotFound(String categoryName,
                                                                  String categoryDescription) {
        ProductCategoryEntity categoryEntity = productCategoryRepository.findByProductCategoryName(categoryName).orElse(null);
        if (categoryEntity == null) {
            categoryEntity = new ProductCategoryEntity();
            categoryEntity.setProductCategoryName(categoryName);
            categoryEntity.setProductCategoryDescription(categoryDescription);
            productCategoryRepository.save(categoryEntity);
        }
        return categoryEntity;
    }

    @Transactional
    private ProductSubCategoryEntity createProductSubCategoryIfNotFound(String subCategoryName,
                                                                        String subCategoryDescription,
                                                                        ProductCategoryEntity productCategoryEntity) {
        ProductSubCategoryEntity subCategoryEntity = productSubCategoryRepository.findByProductSubCategoryName(subCategoryName).orElse(null);
        if (subCategoryEntity == null) {
            subCategoryEntity = new ProductSubCategoryEntity();
            subCategoryEntity.setProductSubCategoryName(subCategoryName);
            subCategoryEntity.setProductSubCategoryDescription(subCategoryDescription);
            subCategoryEntity.setProductCategory(productCategoryEntity);
            productSubCategoryRepository.save(subCategoryEntity);
        }
        return subCategoryEntity;
    }
}

